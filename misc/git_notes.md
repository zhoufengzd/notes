# Git Notes
* git sync is dangerous!
*   git pull --rebase + git push
* git help <command>
```
git repository: local repository <-> remote repository
  origin: remote repo
  master: default local branch repo is created, most likely "the main branch".

  push: local --> remote
  fetch: remote
  pull / rebase: remote -> local

changes:
  add: accept the change
  commit: save to local repository (index, HEAD points to the latest change)
  checkout: take the copy from the source / discard local changes

  revert local: git reset HEAD~1
```

## create repository
```
* create repository in current directory
git init
* no working directory, mainly for starting central repository
git init --bare my-project.git
* make a local git repository from remote repository.
git clone username@host:/path/to/repository

* connect to a remote server
git remote add origin <server url>

* configure git repository or git installation
git config user.name <name>
git config --system core.editor <editor>
```

## branch
```
* create branch
git checkout -b <feature_x>
git branch <DATA-2..>

* switch back to master
git checkout master

* delete the branch
git branch -d feature_x

* rename / reuse branch
git branch -m old_name new_name

* set upstream: -u|--set-upstream-to
git branch -u origin/develop <feature branch>
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
git diff <source_branch> <target_branch>

* tagging
git tag 1.0.0 <1b2e1d63ff: first 10 characters of the commit id>


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
