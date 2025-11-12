from playwright.sync_api import sync_playwright

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
            link_da_vaga = card_vaga.get_attribute("href")
            
            #Continuar aqui, depois de encontrar o link da página, eu acesso ele, busco os dados que quero e depois salva no banco de dados as informações separadas

        next_button.click()

        #(Importante) Esperamos a rede "acalmar"
        # Isso garante que a próxima página carregou antes de tentarmos de novo
        page.wait_for_load_state("networkidle")
        

    browser.close()