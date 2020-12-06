
# CS 321-1 Server Side Programming
### Puthenthara Suresh, Sebin
---

### Application Name
Doodle Board

### URL
[doodle-board.herokuapp.com](https://doodle-board.herokuapp.com/)

### Screenshots
![Screenshot of the website showing the feed page of the website](/src/main/resources/static/images/screenshot1.jpg?raw=true "Feed Page")
![Screenshot of the website showing the doodle editing feature of the website with a drawing of a dragon](/src/main/resources/static/images/screenshot2.jpg?raw=true "Editing Doodles")

### Project Topic/Objective
Doodle board is a pixel-art creation and gallery website. Users can create pixel-art in the browser in a 64x64 pixel canvas using 11 colors and transparent or white color pixels. The drawings are called “doodles” and the posts containing the doodles, their title, and other information are called “doodle posts”. Users can view the doodle posts created by other users, and create, update, and delete doodle posts of their own. The targeted user population is artistically inclined people of all ages, who happen to enjoy minimalistic and pixel art.

### Motivation
I was heavily inspired by some of the artwork I have seen on the website “Pixel Joint.” Various artists collaborate on a giant canvas and produce an impressive looking final piece of art, while working under limitations of limited colors, character proportions, shading rules, etc. on that website’s collaboration challenges. An example of such an artwork can be found at [http://pixeljoint.com/forum/forum_posts.asp?TID=25150](http://pixeljoint.com/forum/forum_posts.asp?TID=25150) (You could click on the images to zoom in and see the details). I believe the restrictions such as the limited color-palette made the artists more creative in using those colors to convey ideas. I was also inspired by the art sharing forums such as DeviantArt and would like to make an online art gallery/social media website like it, but on a smaller scale.

### Components
Spring Boot is used to manage the server-side code for this website. Plugins like Lombok, Thymeleaf, Slf4j are used for additional features, and JPA is used for managing and executing the database operations. 
For the front-end, HTML, Javascript, Jquery, Bootstrap and custom CSS, and P5JS are used. 