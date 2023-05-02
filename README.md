# kotgll
* [About](https://github.com/vadyushkins/kotgll#about)
* [Usage](https://github.com/vadyushkins/kotgll#usage)
  * [From sources](https://github.com/vadyushkins/kotgll#from-sources)
  * [Using JAR](https://github.com/vadyushkins/kotgll#using-jar)
  * [CFG Format Example](https://github.com/vadyushkins/kotgll#cfg-format-example)
  * [RSM Format Example](https://github.com/vadyushkins/kotgll#rsm-format-example)

## About
GLL implementation in Kotlin

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
