# test-fbo-viewport

This project demonstrates an issue while trying to use an FBO alongside with a viewport. It was generated with gdx-liftoff and it's as small and simple as possible.

# Video

Here's a video that demonstrates the issue:

https://youtu.be/Ss2QWExclfg

1. The FBO is bound, and the screen is cleared with the green color.
2. A screenshot of the game (.png) is then rendered to the FBO.
3. The FBO is ended right after that.
4. The screen is cleared again with a dark blue color.
5. The FBO's texture is rendered to the screen.
6. And finally the game's screenshot is rendered to the screen again with transparency, on top of everything, to demonstrate the expected result. 

# Running from Source
```gradlew lwjgl3:run``` 
