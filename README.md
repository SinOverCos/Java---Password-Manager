# Java
My first Java project; a little choppy.

This is a password manager I made because I keep all my information in a text file but felt that the information could be organized better. I tried to make a command-line interface and it recognizes a few commands, such as add, delete, edit, search, and help, and their arguments.

Looking back, this could have been implemented better without the crazy number of if-elif-else blocks. Matching fields to integers and using switch statements would have made a much cleaner implementation.
Comments would've been good to add, too.

One feature I would've liked to add is hiding the encryption key input instead of letting it be typed out and then printing a lot of lines to clear the screen.

**To run:**
- ```cd src/```
- ```javac Account.java Email.java Manager.java RunManager.java Template.java```
- ```java RunManager```
