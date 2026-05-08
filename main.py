import json
import numpy as np
import pandas as pd

DATA_FILE = "data.csv"
CATEGORIES_FILE = "categories.json"

if __name__ == "__main__":
    # Read experimental data
    try:
        data = pd.read_csv(DATA_FILE, keep_default_na=False)
        # Don't consider NA (NullAway) as N/A value
    except FileNotFoundError:
        print("File not found:", DATA_FILE)
        exit(1)

    projects = np.append("all", data["Project"].unique())
    groups = data["Group"].unique()
    warnings = data["Warning"].unique()
    reasons = data["Reason"].unique()
    sources = data["Source"].unique()

    # Map categories and reasons
    try:
        with open(CATEGORIES_FILE, "r") as f:
            categories_to_reasons = json.load(f)
    except FileNotFoundError:
        print("File not found:", CATEGORIES_FILE)
        exit(1)

    categories = list(categories_to_reasons.keys())
    reasons_to_categories = {}
    for category, reasons in categories_to_reasons.items():
        for reason in reasons:
            reasons_to_categories[reason] = category

    # Store categories in data
    data["Category"] = data["Reason"].map(lambda r: reasons_to_categories[r])

    # Select a project to view data
    print("Summarize warnings found for project:")
    for i, project in enumerate(projects):
        print(f"{i + 1}. {project}")

    choice = input(f"Enter project ({1}-{len(projects)}): ")
    try:
        choice = int(choice)
        if choice - 1 in range(len(projects)):
            project = projects[choice - 1]
        else:
            print("Invalid choice:", choice)
            exit(1)

    except ValueError:
        print("Not an integer:", choice)
        exit(1)

    if project == "all":
        print("Data for all projects:")
        filtered = data
    else:
        print("Data for project:", project)
        filtered = data.where(data["Project"] == project)
    filtered["Count"] = 1

    # Aggregate by project and reason
    print("Warnings per project:")
    print(filtered.groupby(["Project"]).count()["Count"])

    # Aggregate by warning and group (table 2)
    print("Warnings found by group and source:")
    print(filtered.groupby(["Group", "Source", "Warning"]).count()["Count"])

    # Aggregate by warning and reason (table 3)
    print("Warnings found by category:")
    print(filtered.groupby(["Warning", "Category"]).count()["Count"])

    # Aggregate by source and category
    print("Categories found by source:")
    print(filtered.groupby(["Source", "Category"]).count()["Count"])

    # Aggregate by project and category (table 4)
    print("Top warnings per project:")
    print(filtered.groupby(["Project", "Warning", "Category"]).count()["Count"])
