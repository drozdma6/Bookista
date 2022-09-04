# How to

This file briefly covers knowledge useful for SP1 project

## Content
  - [Content](#content)
    - [Kotlin](#kotlin)
    - [Git](#git)
      - [Commits](#commits)
      - [Commit types](#commit-types)
      - [Branches](#branches)
      - [Merge requests](#merge-requests)
    - [MVVM](#mvvm)
    - [Dependency Injection](#dependency-injection)
    - [Wireframing](#wireframing)
    - [Vysor](#Vysor)
    - [Running Project](#running-project)

### Kotlin

Kotlin is modern programming language similar to C# among others. When using Kotlin to program Android apps, Kotlin is compiled to bytecode, which is interpreted by a JVM. This means if you really don't know how to write class in Kotlin, you can use Java (although hightly unadviced). 
Kotlin documentation can be found [here](https://kotlinlang.org/docs/home.html).
Or list of Lectures from [BI-KOT](https://courses.fit.cvut.cz/BI-KOT/lectures/index.html) (AFAIK everyone should have access to it).

### Git

I will assume everyone knows how to use git. This part will focus on how to write commits, create branches and merge requests. I recommend using some Git tool like [lazygit](https://github.com/jesseduffield/lazygit), [GitKraken](https://www.gitkraken.com) or anything else.

#### Commits

Commit message blueprint looks as follows: '<type_of_commit>: #<issue_number> <Simple_and_conside_commit_message>' 

#### Commit types 
- FEAT - Commit adding a feature
- FIX - Fixing bug or a mistake
- REF - Refactoring code, not changing any functionality
- SMALL - Reserved for small changed not included in previous 3 types

Please follow this blueprint from easy to read commit messages.

#### Branches

Every Issue has its own branch. Branch name looks as follows: 'ISSUE/#<issue_number>-<consise_issue_description>'. Every commit regarding this issue will be commited on this branch. 

#### Merge requests
When work on issue is done, you will create merge request from branch to master. Title of the merge request will be name of the branch. Assign me (bilybran) as assignee, check 'Delete source branch when merge request is accepted' and submit merge request.

### MVVM

Model-View-ViewModel is recommended app architecture for Android. I recommend to read about it, [for example here](https://www.geeksforgeeks.org/mvvm-model-view-viewmodel-architecture-pattern-in-android/) and looking at some implementation, for example [Login Fragment](https://gitlab.fit.cvut.cz/bilybran/bookista/blob/master/app/src/main/java/com/exwise/bookista/fragment/authentication/LoginFragment.kt). It may seem like a bit of an overkill, but learning it can be very useful.

### Dependency Injection

For dependency injection we will use [Koin](https://insert-koin.io). Setup is already done. When creating new Firebase Repository/Usecase, use firebaseModule, when creating ViewModel viewModelModule.

### Wireframing

I would like to if all wireframes were located in project directory in directory wireframing. I really recommend using [Figma](https://www.figma.com). Its free to use, simple to learn and allows for collaborative sharing.

### Vysor

If you want to use your own phone for debugging, I recommend [Vysor](https://www.vysor.io). After installation you can use your computer for controling your mobile device.

### Running project

Go to [Firebase console](https://console.firebase.google.com/u/2/project/bookista-23af2/overview), project settings. Scroll down and download google-services.json. Save this json to app/ directory. This should be enough to run the project.