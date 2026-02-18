Loggers are simple tools to log what your mod is doing. I've pulled a neat trick and made it so that you don't have to add them as a parameter to every method you call down the line.
# How to use a logger
## Where do loggers come from?
Each mod and each feature come with a logger. You can add them to whatever you need to add them to for your own purposes. A mod's logger has the name of the mod prefixed at the start of it, and a feature's logger has the name of the feature added after that. When walking through a specific instance of a chunk of logic, you'll want to attach a "case" logger, which is a logger that has a UUID specific to that instance of the chunk of logic appended to it.
Mod logger example:
```
[tim_core]: Hello from Tim Core!
```
Feature logger example:
```
[tim_core][exp_all]: Hello from Tim Core's ExpAll feature!
```
Case logger example:
```
[tim_core][exp_all][ac566a4f-a57e-4dbf-a7a6-04fbf5da8081]: Hi from a specific run of some logic in the ExpAll feature!
[tim_core][exp_all][ac566a4f-a57e-4dbf-a7a6-04fbf5da8081]: You can tell I'm related to the above log!
[tim_core][exp_all][54a1a41c-e792-4a00-8c00-4cc11eb65025]: I'm my own iteration of the above logic, or some other logic.
```
## How do I attach a logger?
Usually, you won't use mod or feature loggers directly, instead you'll attach a case logger from your mod or feature to the main method responsible for handling some unit of logic