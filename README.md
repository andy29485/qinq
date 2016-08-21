# Qinq
Current Release:
[![Github Release](http://img.shields.io/github/release/Andy29485/qinq.svg)]
(https://github.com/Andy29485/qinq/releases/latest)

## Licence
This project is distributed under the GPLv3 Licence.
[![GPLv3 License](http://img.shields.io/badge/license-GPLv3-blue.svg)]
(https://www.gnu.org/copyleft/gpl.html)

## Contributors
- Andriy Zasypkin - head developer / the bad graphics designer / all other
  things
- Nelson Chen - contributed a question
- Jason Tufano - contributed an idea for a question

## How To Use

### Playing
1. Start game(run the .jar file)
2. Each player must connect to address specified on the screen using a
   (modern) web browser
  - **be sure to include the port** (the colon and the numbers after it)
3. Players must chose a name and click the `create player` button
5. (**Optional**) Select questions for this game
  1. Click options on the main window of the program(**not** in a browser)
  2. Select the check boxes for the categories witch you wish to include
  3. Add custom questions in the `Custom Questions` text area 
    - One question per line
  4. Click Back
6. Click Start
7. Players should follow the instructions(intuitive) on the web page.
  - Answer questions and click submit
  - Vote: click the answers they like
  - Etc...

### Create Custom Question Categories
1. In the directory with the jar, create another directory named `questions`
2. For each category you wish to add, create a text file
   named `<category-name>.txt`
3. Add on question per line to the category file you believe the question
   belongs in
   - The same question can exist in multiple categories

### Start internet session(slower)
Sometimes you want to play with people that are not on your local network
(aka another house). For this to work there needs to be a web-server that
connects non-local clients with your computer. See `web/` dir for more info.
1. Go to options.
2. Find the `Remote Server Connector`.
3. (opt) Change the URL to point to your desired server.
4. Click Start
5. Click Back
  - A new URL should have appeared in the gray box.
6. Have clients direct their Web Browsers to that URL.

## TODO
| Item | Status |
| ---- | :----: |
| Come up with more questions | outsourcing |
| Player images - not just colours | considering |
| Allow internet based connecting(non-local) | complete |
| View results on web-end | complete |
| Round/Game results | complete |
| Scores | complete |
| Make web-end look better | complete |
| Allow selection of categorised answers | complete |
| Allow addition of custom categories | complete |
| End timer if action was complete | complete |
| Allow kicking of players | complete |
| Make web-end look **even** better | dropped |

## Dependencies(for compilation)
- [Jetty 9] (http://www.eclipse.org/jetty)
