# Circle Drawer

This project was an attempt to visually showcase how a neural network learns by training one to draw a circle, and then physically making the results in real life. As an art project, it faded the line between the virtual and the real world.

As the network evolved, I took samples of circles that the network created (which started out as squiggles, of course) and generated G-code so that they could be drawn on paper using a CNC router with a marker attached. These samples would then be shown in sequence from start to finish, showing the evolution from squiggle to circle.

## What's Included

This package includes the ANJI source and compiled code as well as a new evolution mode. This new evolution mode evolves the neural nets to draw something resembling a circle. Tested with Java 10. The network output folder must be changed in `CircleFitnessFunction.java`.

## What's Not Included

Unfortunately, the circle display program written in Processing did not survive the trip into cyberspace. Additionally, the program that converted the circles into G-code and the program that sorted through the output circles were lost as well. 

## Notes

Created for Jeremy Boyle's SA0110 at the University of Pittsburgh, Fall 2018. 

Lisenced under GNU General Public Lisence V2.0
