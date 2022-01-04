# Git Notes
* git sync is dangerous! be careful with git publish!
*   git pull --rebase + git push
* git help <command>

## basic concept:
* repository (project): local <> remote (origin)
* branch: feature branch. like master / develop / feature-x
* action:
    * push: local --> remote
    * fetch: remote
    * pull / rebase: remote -> local
    * changes:
        * add: accept the change
        * commit: save to local repository
        * checkout: take the copy from the source
        * revert local: git reset HEAD~1

## configuration
```
git config user.name <name>
git config --system core.editor <editor>

* default config
git config --global pull.rebase true
git config --global fetch.prune true
git config --global diff.colorMoved zebra
```

## repository operation
* set up central repo: `git init --bare my-project.git`
* set up local repo in one of the following:
```
git init && git remote add origin <server url|/path/to/repository>
git clone username@host:/path/to/repository
```
* check origin: `git remote show origin`


## branch operation
```
* create branch and set upper branch:
git checkout -b <feature-x>
git branch -u origin/develop <feature-x>

* check out feature branch
git checkout --track origin/<feature-x>

* switch back to master or other branch
git checkout master

* check out a commit in current branch
git checkout tags/<tag>

* delete the branch
git branch -d feature_x

* rename / reuse branch
git branch -m old_name new_name
```

## changes
```
git status
git add *
git commit -m "commit message"

* update on current branch
git pull origin master

* pushing changes from particular branch. If changes are in work branch, it won't be merged automatically
git push origin <branch>
git push origin local-name:remote-name  ## push to another branch

* merge
git merge <branch>

* revert
git revert -m 1 <commit-hash>
git checkout .  ## revert all local uncommitted changes
git reset --hard HEAD ## revert all changes
git clean -fdx  ## remove all local untracked files
-- simple way
git stash
git stash drop

* diff
git diff [<source_branch> <target_branch>]

* tagging
git tag <tag_name> <1b2e1d63ff: first 10 characters of the commit id>
** remove tag
git tag -d <tag_name>
git push origin :refs/tags/<tag_name>
-- remove remote tag in current branch
git push --delete origin <tag>

* check changes
git log --author=bob
git log --follow -p $file

* undo / replace local changes
git checkout -- <filename>

git fetch origin
git reset --hard origin/<master branch>

* remove untracked directory, ignored or unignored files
git clean -fd
git clean -fX
git clean -fx

* shelve changes
git add .
git stash  # push changes to shelve
git stash apply # pop changes from shelve

* list branch with author
git for-each-ref --format='%(committerdate) %09 %(authorname) %09 %(refname)' | sort -k5n -k2M -k3n -k4n
```
