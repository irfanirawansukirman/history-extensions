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

## Table of Contents
##### [1.Activity Scope](#activity-scope)
##### [2.Fragment](https://github.com/irfanirawansukirman/history-extensions#fragment)
##### [3.LiveData](https://github.com/irfanirawansukirman/history-extensions#livedata) 
##### [4.UI](https://github.com/irfanirawansukirman/history-extensions#usage-in-kotlin)
- [View](https://github.com/irfanirawansukirman/history-extensions#view)
- [TextView](https://github.com/irfanirawansukirman/history-extensions#textview)
- [RecyclerView](https://github.com/irfanirawansukirman/history-extensions#recyclerview) 
- [ImageView](https://github.com/irfanirawansukirman/history-extensions#encryptentity) 
##### [5.Conversion](https://github.com/irfanirawansukirman/history-extensions#imageview)

## Activity Scope
Show toast message on Activity Class
```
private var toast: Toast? = null
fun AppCompatActivity.showToast(message: String) {
    toast?.cancel()
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast?.show()
}
```
##### How to use?
```
class LoremClass: AppCompatActivity() {
    
     fun someFunc() {
          showToast("Your message")
     } 
}
```