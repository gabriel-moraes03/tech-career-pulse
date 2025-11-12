import dotenv
import psycopg2
import os

dotenv.load_dotenv()

DB_NAME = os.getenv("POSTGRES_DB")
DB_USER = os.getenv("POSTGRES_USER")
DB_PASSWORD = os.getenv("POSTGRES_PASSWORD")
DB_HOST = "localhost"
DB_PORT = "5432"

try:

    conn = psycopg2.connect(user=DB_USER, password=DB_PASSWORD, host=DB_HOST, port=DB_PORT, database=DB_NAME)

    print("🎉 Conexão com o PostgreSQL bem-sucedida!")

    conn.close()

except psycopg2.Error as e:
    
    print(f"❌ Erro ao conectar ao PostgreSQL: {e}")