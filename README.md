# kotgll
[![Build](https://github.com/vadyushkins/kotgll/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/vadyushkins/kotgll/actions/workflows/build.yml)
[![Test](https://github.com/vadyushkins/kotgll/actions/workflows/test.yml/badge.svg?branch=main)](https://github.com/vadyushkins/kotgll/actions/workflows/test.yml)

* [About](https://github.com/vadyushkins/kotgll#about)
* [Usage](https://github.com/vadyushkins/kotgll#usage)
  * [From sources](https://github.com/vadyushkins/kotgll#from-sources)
  * [Using JAR](https://github.com/vadyushkins/kotgll#using-jar)
  * [CFG Format Example](https://github.com/vadyushkins/kotgll#cfg-format-example)
  * [RSM Format Example](https://github.com/vadyushkins/kotgll#rsm-format-example)
* [Performance](https://github.com/vadyushkins/kotgll#performance)
  * [Graphs](https://github.com/vadyushkins/kotgll#graphs)
  * [Grammars](https://github.com/vadyushkins/kotgll#grammars)
  * [Results](https://github.com/vadyushkins/kotgll#results)
  * [More results](https://github.com/vadyushkins/kotgll#more-results)

> Note: project under heavy development!

## About
**kotgll** is an open-source project for implementing the GLL algorithm and its modifications in Kotlin

## Usage

### Command Line Interface

```text
Usage: kotgll options_list
Options: 
    --input -> Input format (always required) { Value should be one of [string, graph] }
    --grammar -> Grammar format (always required) { Value should be one of [cfg, rsm] }
    --sppf [ON] -> Sppf mode { Value should be one of [on, off] }
    --inputPath -> Path to input txt file (always required) { String }
    --grammarPath -> Path to grammar txt file (always required) { String }
    --outputPath -> Path to output txt file (always required) { String }
    --help, -h -> Usage info
```

### From sources

#### Step 1. Clone repository

`git clone https://github.com/vadyushkins/kotgll.git`

or 

`git clone git@github.com:vadyushkins/kotgll.git`

or 

`gh repo clone vadyushkins/kotgll`

#### Step 2. Go to the folder

`cd kotgll`

#### Step 3. Run the help command

`gradle run --args="--help"`

You will see the ["Options list"](https://github.com/vadyushkins/kotgll#command-line-interface) message.

#### Example

```text
gradle run --args="--input graph --grammar rsm --sppf off --inputPath src/test/resources/cli/TestGraphReadWriteCSV/dyck.csv --grammarPath src/test/resources/cli/TestRSMReadWriteTXT/dyck.txt --outputPath ./result.txt"
```

### Using JAR

#### Step 1. Download the latest JAR

```text
curl -L -O https://github.com/vadyushkins/kotgll/releases/download/v1.0.0/kotgll-1.0.0.jar
```

#### Step 2. Run JAR with Java

```text
java -jar kotgll-1.0.0.jar --input graph --grammar rsm --sppf off --inputPath src/test/resources/cli/TestGraphReadWriteCSV/dyck.csv --grammarPath src/test/resources/cli/TestRSMReadWriteTXT/dyck.txt --outputPath ./result.txt
```
### CFG Format Example

```text
StartNonterminal("S")
Nonterminal("S") -> Terminal("subClassOf_r") Nonterminal("S") Terminal("subClassOf")
Nonterminal("S") -> Terminal("subClassOf_r") Terminal("subClassOf")
Nonterminal("S") -> Terminal("type_r") Nonterminal("S") Terminal("type")
Nonterminal("S") -> Terminal("type_r") Terminal("type")
```

### RSM Format Example

```text
StartState(id=0,nonterminal=Nonterminal("S"),isStart=true,isFinal=false)
State(id=0,nonterminal=Nonterminal("S"),isStart=true,isFinal=false)
State(id=1,nonterminal=Nonterminal("S"),isStart=false,isFinal=false)
State(id=4,nonterminal=Nonterminal("S"),isStart=false,isFinal=false)
State(id=3,nonterminal=Nonterminal("S"),isStart=false,isFinal=true)
State(id=2,nonterminal=Nonterminal("S"),isStart=false,isFinal=false)
State(id=6,nonterminal=Nonterminal("S"),isStart=false,isFinal=true)
State(id=5,nonterminal=Nonterminal("S"),isStart=false,isFinal=false)
TerminalEdge(tail=0,head=1,terminal=Terminal("subClassOf_r"))
TerminalEdge(tail=0,head=4,terminal=Terminal("type_r"))
TerminalEdge(tail=1,head=3,terminal=Terminal("subClassOf"))
NonterminalEdge(tail=1,head=2,nonterminal=Nonterminal("S"))
TerminalEdge(tail=4,head=6,terminal=Terminal("type"))
NonterminalEdge(tail=4,head=5,nonterminal=Nonterminal("S"))
TerminalEdge(tail=2,head=3,terminal=Terminal("subClassOf"))
TerminalEdge(tail=5,head=6,terminal=Terminal("type"))
```

## Performance

The GLL algorithm has been modified to support graph input.
The proposed modification has been evaluated on several real graphs for the scenario of finding all pairs of reachability.

**Machine configuration**: PC with Ubuntu 20.04, Intel Core i7-6700 3.40GHz CPU, DDR4 64Gb RAM.

**Enviroment configuration**: 
* Java HotSpot(TM) 64-Bit server virtual machine (build 15.0.2+7-27, mixed mode, sharing).
* JVM heap configuration: 55Gb both xms and xmx.

### Graphs

The graph data is selected from [CFPQ_Data dataset](https://formallanguageconstrainedpathquerying.github.io/CFPQ_Data).

A detailed description of the graphs is listed bellow.

#### RDF analysis graphs

| Graph name   |   \|*V*\| |     \|*E*\| |  #subClassOf |      #type |  #broaderTransitive |
|:------------|----------:|------------:|-------------:|-----------:|--------------------:|
| [Enzyme](https://formallanguageconstrainedpathquerying.github.io/CFPQ_Data/graphs/data/enzyme.html#enzyme)       |    48 815 |      86 543 |        8 163 |     14 989 |               8 156 |
| [Eclass](https://formallanguageconstrainedpathquerying.github.io/CFPQ_Data/graphs/data/eclass.html#eclass) |   239 111 |     360 248 |       90 962 |     72 517 |                   0 |
| [Go hierarchy](https://formallanguageconstrainedpathquerying.github.io/CFPQ_Data/graphs/data/go_hierarchy.html#go-hierarchy) |    45 007 |     490 109 |      490 109 |          0 |                   0 | 
| [Go](https://formallanguageconstrainedpathquerying.github.io/CFPQ_Data/graphs/data/go.html#go)           |   582 929 |   1 437 437 |       94 514 |    226 481 |                   0 |
| [Geospecies](https://formallanguageconstrainedpathquerying.github.io/CFPQ_Data/graphs/data/geospecies.html#geospecies)   |   450 609 |   2 201 532 |            0 |     89 065 |              20 867 |  
| [Taxonomy](https://formallanguageconstrainedpathquerying.github.io/CFPQ_Data/graphs/data/taxonomy.html#taxonomy)     | 5 728 398 |  14 922 125 |    2 112 637 |  2 508 635 |                   0 |

### Grammars

All queries used in evaluation are variants of same-generation query.
The inverse of an ```x``` relation and the respective edge is denoted as ```x_r```.

<br/>

Grammars used for **RDF** graphs:

**G<sub>1</sub>**
```
S -> subClassOf_r S subClassOf | subClassOf_r subClassOf 
     | type_r S type | type_r type
```

The representation of **G<sub>1</sub>** context-free grammar in the repository can be found [here](https://github.com/vadyushkins/kotgll/blob/main/src/test/resources/cli/TestCFGReadWriteTXT/g1.txt).

The representation of **G<sub>1</sub>** context-free grammar as recursive automaton in the repository can be found [here](https://github.com/vadyushkins/kotgll/blob/main/src/test/resources/cli/TestRSMReadWriteTXT/g1.txt).

### Results

The results of the **all pairs reachability** queries evaluation on graphs related to **RDF analysis** are listed below.

In each row, the best mean time in seconds is highlighted in **bold**.

| Graph        	|  [CFG](https://github.com/vadyushkins/kotgll/tree/main/src/main/kotlin/org/kotgll/cfg/graphinput/withoutsppf)  	|    [RSM](https://github.com/vadyushkins/kotgll/tree/main/src/main/kotlin/org/kotgll/rsm/graphinput/withoutsppf)    	| [GLL4Graph](https://github.com/FormalLanguageConstrainedPathQuerying/GLL4Graph) 	|
|--------------	|:-----:	|:---------:	|:---------:	|
| [Enzyme](https://formallanguageconstrainedpathquerying.github.io/CFPQ_Data/graphs/data/enzyme.html#enzyme)       	| 0.107 	| **0.044** 	|      0.22 	|
| [Eclass](https://formallanguageconstrainedpathquerying.github.io/CFPQ_Data/graphs/data/eclass.html#eclass)       	|  0.94 	|  **0.43** 	|       1.5 	|
| [Go hierarchy](https://formallanguageconstrainedpathquerying.github.io/CFPQ_Data/graphs/data/go_hierarchy.html#go-hierarchy) 	|   4.1 	|   **3.0** 	|       3.6 	|
| [Go](https://formallanguageconstrainedpathquerying.github.io/CFPQ_Data/graphs/data/go.html#go)           	|   3.2 	|  **1.86** 	|      5.55 	|
| [Geospecies](https://formallanguageconstrainedpathquerying.github.io/CFPQ_Data/graphs/data/geospecies.html#geospecies)   	|  0.97 	|  **0.34** 	|      2.89 	|
| [Taxonomy](https://formallanguageconstrainedpathquerying.github.io/CFPQ_Data/graphs/data/taxonomy.html#taxonomy)     	|  31.2 	|  **14.8** 	|      45.4 	|

#### More results

More results, but in raw form, can be found in repository [kotgll_benchmarks](https://github.com/vadyushkins/kotgll_benchmarks/)
