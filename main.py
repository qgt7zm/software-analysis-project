import pandas as pd

DATA_FILE = "data.csv"

if __name__ == "__main__":
    try:
        data = pd.read_csv(DATA_FILE)
    except FileNotFoundError:
        print("File not found:", DATA_FILE)
        exit(1)
    
    print(data)