# Evolving Predators for Population Stability
Adam Klein and Adante Ratzlaff

Stable Pop simulates population dynamics between an evolving predator species and a non-evolving prey species across one or more geographically isolated areas.  This project was created to challenge assumptions in the "evolutionary arms race" model of population dynamics.

The arms race model is often used in textbooks and nature documentaries to explain how two species, such as a predator and its prey, may develop adaptations as countermeasures for each others' adaptations.  Problem is, this model implies that both species must evolve in step to remain in balance.  If the predators somehow evolve more quickly than their prey, the predators will deplete their food supply and go extinct.

With Stable Pop, we were able to debunk the implications of the arms race model.  If predator and prey populations are dispersed across several areas with only limited migration between areas, predators that deplete their food supplies and die will be replaced by less-voracious predators, counteracting the scenario predicted by the basic arms-race model.

## Running Stable Pop
### Experimental Parameters
You can set your own experimental parameters inside Main.java, including the number of geographic areas, starting population sizes, and other factors that affect predation, reproduction, and evolution.  These parameters are clearly labeled at the start of the code, along with some explanations.

Once you have finished setting parameters, compile Main.java and any other Java files you haven't compiled.  You can then run Main to start the experiment.

### Output Formatting
Stable Pop outputs data as two files:  experimental settings are recorded in a .txt file and results in a .csv file.  You can set the name for both of these files in Main.java.

For your convenience, we included a Python script called graphParse.py which can be used to put your .csv file in a more human-friendly, Excel-ready format.  In its current state, graphParse reads a file named "output.csv" and writes to a file named "graph.csv".  You can use graph.csv in tandem with one of our Excel templates, graphTemplate.xltx or graphTemplate20k.xltx, to take advantage of some pre-made Excel/Google Sheets formulas and graphs.