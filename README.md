#The Open Script Repository
The Open Script Repository allows non-ranked script writers to publish their scripts to SDN.
<p>
If you have a script that you would like to publish, read on.

###Setting Up
1. Fork this repository
2. Add the remote
    - `git remote add origin https://github.com/[your github name]/powerbot.git`
3. Fetch the repository
    - `git fetch origin`
4. Change your working branch to the 'scripts' branch
    - `git checkout scripts`
5. Commit and push normally when making any changes
6. Make a Pull Request on the 'scripts' branch, which will be evaluated before being accepted

###Guidelines for Scripts
- Scripts **must** be packaged
- The parent package **must** be your powerbot username (e.g. coma/scripts/Script.java)
- Scripts **must** have an `author` tag in the script `Properties` with your powerbot username
- Scripts **must** have a `topic` tag with a thread ID corresponding to your script thread in the [Projects](http://www.powerbot.org/community/forum/55-projects/) section
- Failure to meet any of these guidelines will result in a denied pull request