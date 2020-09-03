# CS 321-1 Server Side Programming
### Puthenthara Suresh, Sebin
---

### Application Name
Doodle Board

### Project Topic/Objective
Doodle Board is a pixel-art creation and gallery website. Users can create pixel-art in the browser in a 32x32 pixel canvas using 11 colors + 1 “transparent” color. Users can “like” artworks, and these likes for each image will be counted. There will be an explore/home feed where all the artwork will be displayed either chronologically or sorted by number of likes. Users can also download artworks. You could also visit each user’s profile from a link under their artwork. The website will use HTML5 canvas element and JavaScript to draw the art, as well as convert the drawing to a string of hexadecimal values that can be sent to the server and stored in a database. Functions for converting such a string back to a canvas element drawing will also be implemented.

(If time allows, a giant shared canvas feature would also be implemented – A single user can claim a 64x64 pixel canvas at a time and draw in there, and the final result would be a giant canvas of artworks from various users.)


### Motivation
I was heavily inspired by some of the artwork I have seen on the website “Pixel Joint.” Various artists collaborate on a giant canvas and produce an impressive looking final piece of art, while working under limitations of limited colors, character proportions, shading rules, etc. on that website’s collaboration challenges. An example of such an artwork can be found at [http://pixeljoint.com/forum/forum_posts.asp?TID=25150](http://pixeljoint.com/forum/forum_posts.asp?TID=25150) (You could click on the images to zoom in and see the details). I believe the restrictions such as the limited color-palette made the artists more creative in using those colors to convey ideas. I was also inspired by the art sharing forums such as DeviantArt and would like to make an online art gallery/social media website like it, but on a smaller scale.

### Server-Side Components
The login information would be stored in a database.

The profile and user information when you visit a user’s profile would be populated using information from a database.

Each artwork, the artist’s username/id, and the “like” count will be stored in a database.

The color palette – the hexadecimal values for each of the colors – would be also stored and accessed from a database

