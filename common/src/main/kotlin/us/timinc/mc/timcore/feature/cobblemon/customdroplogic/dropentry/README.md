---
aliases:
  - Adds a new drop type that drops a random entry from an item tag.
---
```table-of-contents
title: 
style: nestedList # TOC style (nestedList|nestedOrderedList|inlineFirstLevel)
minLevel: 0 # Include headings from the specified level
maxLevel: 0 # Include headings up to the specified level
include: 
exclude: 
includeLinks: true # Make headings clickable
hideWhenEmpty: false # Hide TOC if no headings are found
debugInConsole: false # Print debug info in Obsidian console
```
## Overview
Base mod has a drop type that drops an item with optional data on it. Someone requested being able to drop items, randomly, from an evenly-weighted pool of all items in an item tag. This does that.
## Simple Example
```JSON
{
    "drops": {
        "amount": 1,
        "entries": [
            {
                "itemTag": "minecraft:fishes"
            }
        ]
    }
}
```
Basically, replace the `item` and `components` with `itemTag`. The rest of the properties from base mod's item drop are the same.