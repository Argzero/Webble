Rules of the game:

Left = move spiraling inward;
Right = move spiraling outward;
Up = move outward;
Down = move inward;

1. Collect enemies;
2. Dont die (by letting the enemies eat your food at the center);

----------------------------------------------------
Implemented the usage of an arraylist, due to the necessary effectiveness of its usage for later things which I plan to implement.

Springs are created with flavor lines added to the center to make the web look more 'web-y'.

Hooke's law is indeed implemented with a damping force as well.

All spiral and radial lines are implemented (they acta as the springs).

Clicking moves the original design spider image (self-designed) to a particular location on the grid of web points--specifically to the point nearest the mouse when a click is released if the mouse is close enough to any of the points.

Side note: I hope to, if I have the free time, implement a game element as well, where the spider can crawl across the web to collect enemies approaching the center. The clicking to interact would be changed such that the 'position'/'origin' of the force placed on any clicked node would auto-revert to the center at any given time.

This is a readMe.