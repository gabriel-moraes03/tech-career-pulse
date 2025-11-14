from playwright.sync_api import sync_playwright
from urllib.parse import urlparse, quote
import dotenv
import psycopg2
import os
import re
import uuid
import datetime
import time
import random

dotenv.load_dotenv()

def salvar_vaga(conn, dados_vaga):
    sql = """
        INSERT INTO vaga_bruta (id, titulo, descricao, empresa, localizacao, url, data_coleta, processada, modelo, palavra_chave)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        ON CONFLICT (url) DO NOTHING;
    """

    try:
        cur = conn.cursor()
        cur.execute(sql, (
            dados_vaga['id'],
            dados_vaga['titulo'],
            dados_vaga['descricao'],
            dados_vaga['empresa'],
            dados_vaga['localizacao'],
            dados_vaga['url'],
            dados_vaga['data_coleta'],
            dados_vaga['processada'],
            dados_vaga['modelo'],
            dados_vaga['palavra_chave']
        ))
        conn.commit()
        print(f"     [DB] Vaga salva: {dados_vaga['titulo'][:40]}...")
    
    except (Exception, psycopg2.DatabaseError) as error:
        print(f"     [DB] ERRO ao salvar vaga {dados_vaga['url']}: {error}")
        conn.rollback()
    
    finally:
        if cur:
            cur.close()

LISTA_PESQUISA = ["desenvolvedor", "developer", "software", "cientista de dados", "analista de dados", "engenheiro de dados", "machine learning"]
RECURSOS_BLOQUEADOS = re.compile(r"\.(css|jpg|jpeg|png|svg|woff|woff2)(\?.*)?$")

db_conn = None

try:
    db_conn = psycopg2.connect(
        host="localhost",
        database=os.getenv("POSTGRES_DB"),
        user=os.getenv("POSTGRES_USER"),
        password=os.getenv("POSTGRES_PASSWORD"),
        port="5432"
    )

    print("--- CONEXÃO COM POSTGRES ABERTA ---")
    
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
    
        context = browser.new_context(
            user_agent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36"
        )

        page = context.new_page()

        # Pra agilizar, bloqueia estilos, fontes, imagens, etc
        def bloquear_recursos(route):
            if RECURSOS_BLOQUEADOS.search(route.request.url):
                route.abort() # Aborta (bloqueia) a requisição
            else:
                route.continue_() # Deixa passar (ex: HTML, JSON, JS)

        # 5. APLIQUE A REGRA de bloqueio
        page.route("**/*", bloquear_recursos)

        for termo in LISTA_PESQUISA:
            print(f"--- INICIANDO BUSCA POR: '{termo}' ---")

            termo_formatado = quote(termo)
            url_busca = f"https://portal.gupy.io/job-search/term={termo_formatado}"

            page.goto(url_busca)

            next_button = page.locator('[aria-label="Próxima página"]')

            while True:

                loc_cads_vagas = page.locator('[aria-label^="Ir para vaga "]')
                lista_cards_vagas = loc_cads_vagas.all()
                
                for card_vaga in lista_cards_vagas:
                    link_da_vaga = ""
                    try:
                        titulo_vaga = card_vaga.locator('h3').text_content()
                        modelo_vaga = card_vaga.locator('[aria-label^="Modelo de trabalho "] span').text_content()
                        localizacao = card_vaga.locator('[aria-label^="Local de trabalho"] span').text_content()

                        if(localizacao == "Não informado"):
                            localizacao = None

                        link_da_vaga = card_vaga.get_attribute("href")
                        
                        page_vaga = context.new_page()
                        page_vaga.route("**/*", bloquear_recursos)
                        page_vaga.goto(link_da_vaga)

                        seletor_req = '[data-testid="section-Requisitos e qualificações-title"] + div ul li'
                        seletor_resp = '[data-testid="section-Responsabilidades e atribuições-title"] + div ul li'
                        seletor_excessao_p_resp = '[data-testid="section-Responsabilidades e atribuições-title"] + div p'
                        seletor_excessao_p_req = '[data-testid="section-Requisitos e qualificações-title"] + div p'
                        seletor_final_ou = f"{seletor_req}, {seletor_resp}, {seletor_excessao_p_resp}, {seletor_excessao_p_req}"
                    
                        loc_combinado = page_vaga.locator(seletor_final_ou)
                        lista_completa = loc_combinado.all_text_contents()
                    
                        descricao = "(separador)".join(lista_completa)

                        parsed_url = urlparse(link_da_vaga)
                        empresa_url = f"{parsed_url.scheme}://{parsed_url.netloc}"

                        page_empresa = context.new_page()
                        page_empresa.route("**/*", bloquear_recursos)
                        page_empresa.goto(empresa_url)

                        nome_empresa = page_empresa.locator('h1').first.text_content(timeout=15000)

                        page_empresa.close()
                        page_vaga.close()

                        dados_para_salvar = {
                            "id": str(uuid.uuid4()),
                            "titulo": titulo_vaga,
                            "descricao": descricao,
                            "empresa": nome_empresa,
                            "localizacao": localizacao,
                            "url": link_da_vaga,
                            "data_coleta": datetime.datetime.now(),
                            "processada": False,
                            "modelo": modelo_vaga,
                            "palavra_chave": termo
                        }

                        salvar_vaga(db_conn, dados_para_salvar)

                        pausa = random.uniform(0.5, 1.5) # Pausa entre 0.5 e 1.5 segundos
                        time.sleep(pausa)
                    
                    except Exception as e:
                        print(f"    ERRO ao processar a vaga {link_da_vaga}: {e}")
                        # Garante que as abas são fechadas mesmo se der erro
                        if 'page_vaga' in locals() and not page_vaga.is_closed():
                            page_vaga.close()
                        if 'page_empresa' in locals() and not page_empresa.is_closed():
                            page_empresa.close()

                if not next_button.is_enabled():
                    break

                next_button.click()
                page.wait_for_load_state("domcontentloaded")

            print(f"--- Busca por '{termo}' CONCLUÍDA ---")

        browser.close()
        print("--- SCRAPER FINALIZADO ---")

except (Exception, psycopg2.Error) as error:
    print(f"Erro fatal no script: {error}")

finally:
    if db_conn:
        db_conn.close()
        print("--- CONEXÃO COM POSTGRES FECHADA ---")