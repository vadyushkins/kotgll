# kotgll
* About
* Usage

## About
GLL implementation in Kotlin

## Usage

### Run from sources

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

You will see the following message.

```
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
