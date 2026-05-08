# Software Analysis Project

Final project for CS 6888: Software Analysis and Applications, Spring 2026, at UVA.

## Topic

Analyzing false and true positive warnings produced by [JSpecify](https://jspecify.dev/)
and [NullAway](https://github.com/uber/NullAway).

## Reproducing

### Methods

- See this description of the [experimental setup](docs/Experimental%20Setup.md).
- See this list of the [benchmark libraries](docs/Benchmarks.md).

### Results

- The results are provided in a [spreadsheet](Project%20Data.ods) (for humans) and [CSV file](data.csv) (for programs).
- See this explanation of the [types of warnings](docs/Warnings.md).
- See this detailed analysis of [selected examples](docs/Selected%20Examples.md).
- Source code and build configurations for examples are also [provided](examples).

### Figures

- Create a venv or conda environment (recommended).
- Install the requirements using `pip install -r requirements.txt` or `conda install --file requirements.txt`.
- Run `python3 main.py` and follow the input prompt to view aggregated results.