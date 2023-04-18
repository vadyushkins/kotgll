# kotgll
* [About](https://github.com/vadyushkins/kotgll#about)
* [Usage](https://github.com/vadyushkins/kotgll#usage)
  * [From sources](https://github.com/vadyushkins/kotgll#from-sources)
  * [Using JAR](https://github.com/vadyushkins/kotgll#using-jar)

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
curl -L -O https://github.com/vadyushkins/kotgll/releases/download/test/kotgll-1.0.0.jar
```

#### Step 2. Run JAR with Java

```text
java -jar --input graph --grammar rsm --sppf off --inputPath src/test/resources/cli/TestGraphReadWriteCSV/dyck.csv --grammarPath src/test/resources/cli/TestRSMReadWriteTXT/dyck.txt --outputPath ./result.txt
```
