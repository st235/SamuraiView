<img src="https://raw.githubusercontent.com/st235/SamuraiView/master/images/showcase.gif" width="600" height="228">

# SamuraiView
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.st235/samuraiview/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.st235/samuraiview)

Simple showcase library. Made Samurai great again! ⚔️

## Download

__Important: library was migrated from JCenter to MavenCentral__ 

It means that it may be necessary to add __mavenCentral__ repository to your repositories list

```groovy
allprojects {
    repositories {
        // your repositories

        mavenCentral()
    }
}
```

- Maven

```text
<dependency>
  <groupId>com.github.st235</groupId>
  <artifactId>samuraiview</artifactId>
  <version>X.X</version>
  <type>pom</type>
</dependency>
```

- Gradle

```text
implementation 'com.github.st235:samuraiview:X.X'
```

- Ivy

```text
<dependency org='com.github.st235' name='samuraiview' rev='X.X'>
  <artifact name='expandablebottombar' ext='pom' ></artifact>
</dependency>
```


## Usage

First of all, you should declare your SamuraiView at xml file

```xml
    <github.com.st235.lib_samurai.SamuraiView
        android:id="@+id/samurai_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <androidx.appcompat.widget.AppCompatButton
            android:id="@+id/samurai_next_view"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="end|bottom"
            style="@style/Widget.AppCompat.Button.Borderless.Colored"
            android:text="@string/button_next"
            android:layout_margin="16dp" />
    </github.com.st235.lib_samurai.SamuraiView>
```

Yeap, forget to say: SamuraiView is a viewgroup, so you may add children to it directly in xml

Then to highlight some views just `capture` it!

```kotlin
    Harakiri(into = samuraiView)
            .circle()
            .overlayColorRes(R.color.colorWhite95)
            .frameColorRes(R.color.colorPrimaryDark)
            .frameThickness(2F)
            .margins(20, 20, 20, 20)
            .withTooltip(R.layout.tooltip_share, width = ViewGroup.LayoutParams.MATCH_PARENT)
            .capture(toolbar.findViewById<View>(R.id.action_share))
```

## Screens

<img src="https://raw.githubusercontent.com/st235/SamuraiView/master/images/introduction.png" width="270" height="480"> <img src="https://raw.githubusercontent.com/st235/SamuraiView/master/images/author.png" width="270" height="480"> <img src="https://raw.githubusercontent.com/st235/SamuraiView/master/images/share.png" width="270" height="480">

### Licenses

Photos were taken from [unsplash](https://unsplash.com/)

```text
MIT License

Copyright (c) 2019 Alexander Dadukin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
