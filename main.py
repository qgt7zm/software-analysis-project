import numpy as np
import pandas as pd

DATA_FILE = "data.csv"

if __name__ == "__main__":
    try:
        data = pd.read_csv(DATA_FILE, keep_default_na=False)
        # Don't consider NA (NullAway ) as N/A value
    except FileNotFoundError:
        print("File not found:", DATA_FILE)
        exit(1)

    projects = np.append(data["Project"].unique(), "all")
    groups = data["Group"].unique()
    warnings = data["Warning"].unique()
    reasons = data["Reason"].unique()
    sources = data["Source"].unique()

    # Aggregate by warning and reason
    print("Count warnings and reasons for project:")
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
        print("Warnings for all projects:")
        aggregated = data
    else:
        print("Warnings for project:", project)
        aggregated = data.where(data["Project"] == project)

    aggregated["Count"] = 1
    print(aggregated.groupby(["Warning", "Reason"]).count()["Count"])
