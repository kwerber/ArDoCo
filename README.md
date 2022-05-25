# Evaluation of Word Similarity Measures on ArDoCo

This branch contains the state of ArDoCo that was used to perform the evaluation for my bachelor's thesis.
It also contains information about how to reproduce this evaluation.

## Evaluation

Before launching the evaluation, some things need to be considered:

- The `CommonTextToolsConfig.properties` can be used to decide which measures will be evaluated
- This file is located at: `util/src/main/resources/configs/CommonTextToolsConfig.properties`
- The evaluation only cares about the `ENABLED` settings. Thresholds and other values are ignored.
- If you intend to evaluate the NASARI measure, you need to specify a `babelNet_ApiKey` in this configuration file
	- You also probably want to specify a `cacheFilePath` for Nasari so that the results of BabelNet queries are cached
- The evaluation script assumes that the data sources exist at a very specific location with very specific file names.
- The data sources are assumed to reside under the `tests/data/` directory
- The following file/directory names are expected for the data sources:
	- `sewordsim.sqlite`
	- `glove_cc_840B_300d.sqlite`
	- `glove_wikigiga_300d.sqlite`
	- `glove_wikigiga_200d.sqlite`
	- `glove_wikigiga_100d.sqlite`
	- `glove_wikigiga_50d.sqlite`
	- `glove_twitter_200d.sqlite`
	- `glove_twitter_100d.sqlite`
	- `glove_twitter_50d.sqlite`
	- `glove_twitter_25d.sqlite`
	- `cc.en.300.bin` (for fastText)
	- `crawl-300d-2M-subword.bin` (for fastText)
	- `wiki-news-300d-1M-subword.bin` (for fastText)
	- `wordNet_dict` (the WordNet 3.1 dict/ directory)
	- `nasari_embed_english.sqlite`
	- `nasari_embed_umbc.sqlite`
- If any of these files are missing, the respective evaluation plans are skipped
- The evaluation will generate results on the fly while running and saves them in the `tests/results/json/` directory.
- Should the process be stopped while running, simply restart the evaluation to continue.
- After all evaluation plans are evaluated, csv files are generated in the `tests/results/csv/` directory.

The evaluation process can be started either directly via maven or by using a docker image.

### Using Docker

To start the evaluation using docker, the image needs to be built first.

```shell
docker build -t ardoco .
```

The docker image expects two volumes to exist:

- The results volume at `/ardoco/tests/results/`
- This is where the evaluation results are stored
- The data volume at `/ardoco/tests/data/`
- This is where all data sources must be located

After the image is built, it can be run like any other docker image:

```shell
docker run -v=HOSTDIR:/ardoco/tests/results -v=HOSTDIR:/ardoco/tests/data/ ardoco
```

### Using Maven

To start the evaluation using maven, simply execute the following command:

```shell
mvn -DfailIfNoTests=false -Dtest=ExhaustiveEvaluation test
```

### Data sources

Some of the evaluated measures require data sources like word vector embeddings or other information.
The exact data sources that were used for my thesis can be downloaded from the following DOI:

TODO:LINKHERE

The linked resource also contains the detailed evaluation results some of which where displayed as graphs in my
thesis.

## ArDoCo

This repository is a fork of the official [ArDoCo Core](https://github.com/ArDoCo/Core/) framework.
The original README of this framework is appended below.

### Core
[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/dashboard?id=ArDoCo_Core)

[![Maven Verify](https://github.com/ArDoCo/Core/workflows/Maven%20Verify/badge.svg)](https://github.com/ArDoCo/Core/actions?query=workflow%3A%22Maven+Verify%22)


The goal of this project is to connect architecture documentation and models while identifying missing or deviating elements (inconsistencies).
An element can be any representable item of the model, like a component or a relation.
To do so, we first create trace links and then make use of them and other information to identify inconsistencies.


#### CLI
The Core Project contains a CLI that currently supports to find trace links between PCM Models and Textual SW Architecture Documentation.
The CLI is part of the pipeline module of this project.
The PCM models have to be converted to ontologies using [Ecore2OWL](https://github.com/kit-sdq/Ecore2OWL).
The model can also contain a (java) code model that you can insert using the [CodeModelExtractors](https://github.com/ArDoCo/CodeModelExtractors).

##### Usage
```
usage: java -jar ardoco-core-pipeline.jar
-c,--conf <arg>           path to the additional config file
-h,--help                 show this message
-i,--withimplementation   indicate that the model contains the code model
-m,--model <arg>          path to the owl model
-n,--name <arg>           name of the run
-o,--out <arg>            path to the output directory
-p,--provided             flag to show that ontology has text already
						provided
-t,--text <arg>           path to the text file
```

##### Wiki
For more information about the setup or the architecture have a look on the [wiki](https://github.com/ArDoCo/Core/wiki/Overview).
The wiki is at some points deprecated, the general overview and setup should still hold.

##### Case Studies / Benchmarks
To test the Core, you could use case studies and benchmarks provided in ..
* [ArDoCo Benchmark](https://github.com/ArDoCo/Benchmark)
* [SWATTR](https://github.com/ArDoCo/SWATTR)

##### Attribution
The base for this project is based on the master thesis [Linking Software Architecture Documentation and Models](https://doi.org/10.5445/IR/1000126194).
