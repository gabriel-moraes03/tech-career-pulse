from playwright.sync_api import sync_playwright
from urllib.parse import urlparse

URL = "https://portal.gupy.io/job-search/term=desenvolvedor"

with sync_playwright() as p:
    browser = p.chromium.launch(headless=False)
    page = browser.new_page()
    page.goto(URL)

    next_button = page.locator('[aria-label="Próxima página"]')

    while next_button.is_enabled():

        loc_cads_vagas = page.locator('[aria-label^="Ir para vaga "]')
        lista_cards_vagas = loc_cads_vagas.all()
        
        for card_vaga in lista_cards_vagas:
            try:
                titulo_vaga = card_vaga.locator('h3').text_content()
                modelo_vaga = card_vaga.locator('[aria-label^="Modelo de trabalho "] span').text_content()
                localizacao = card_vaga.locator('[aria-label^="Local de trabalho"] span').text_content()

                if(localizacao == "Não informado"):
                    localizacao = None

                link_da_vaga = card_vaga.get_attribute("href")
                
                page_vaga = browser.new_page()
                page_vaga.goto(link_da_vaga)

                seletor_req = '[data-testid="section-Requisitos e qualificações-title"] + div ul li'
                seletor_resp = '[data-testid="section-Responsabilidades e atribuições-title"] + div ul li'
                seletor_final_ou = f"{seletor_req}, {seletor_resp}"
            
                loc_combinado = page_vaga.locator(seletor_final_ou)
                lista_completa = loc_combinado.all_text_contents()
            
                descricao = "(separador)".join(lista_completa)

                parsed_url = urlparse(link_da_vaga)
                empresa_url = f"{parsed_url.scheme}://{parsed_url.netloc}"

                page_empresa = browser.new_page()
                page_empresa.goto(empresa_url)

                nome_empresa = page_empresa.locator('h1').first.text_content(timeout=15000)

                page_empresa.close()
                page_vaga.close()
            
            except Exception as e:
                print(f"    ERRO ao processar a vaga {link_da_vaga}: {e}")
                # Garante que as abas são fechadas mesmo se der erro
                if 'page_vaga' in locals() and not page_vaga.is_closed():
                    page_vaga.close()
                if 'page_empresa' in locals() and not page_empresa.is_closed():
                    page_empresa.close()

        next_button.click()
        page.wait_for_load_state("networkidle")
        

    browser.close()