# History Extension Library

This is a collection of android kotlin extensions that I often use while developing android applications. I like open source, therefore I hope this can be useful and help speed up the application development process and reduce code boilerplate for android developers.

## Getting Started

This library uses androidX. So, before you use this library, please migrate your project dependencies to androidX and you can see the documentation in [here](https://medium.com/androiddevelopers/migrating-to-androidx-tip-tricks-and-guidance-88d5de238876).

### Installing

Add it to your build.gradle (project):

```
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

and add it to your build.gradle (module):

```
implementation 'com.github.irfanirawansukirman:history-extensions:0.0.4'
```