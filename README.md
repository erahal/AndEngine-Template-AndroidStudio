# AndEngine-Template-AndroidStudio
A lot of people are havving problem integrating AndEngine with AndroidStudio. So this is a Template Project for AndEngine, to start developing a game with Andengine. Included are the Box2D extension and the DebugDraw to debug the physics. The project contains a MainActivity.Java Class where a simple image have been used to put a sprite on the screen.

**This project is compiled with:**
* compileSdkVersion 23
* buildToolsVersion '23.0.2'

**To Deacticate physics library comment the two physics project lines in GameModule's Gradle file as shown below:**
```
dependencies {
    compile project(':andEngine4')
    //compile project(':andEnginePhysicsBox2DExtension')
    compile 'com.google.android.gms:play-services-analytics:8.4.0'
    compile 'com.google.android.gms:play-services-ads:8.4.0'
    //compile project(':AndEngineDebugDrawExtension')
    compile 'com.android.support:support-v4:23.1.1'
}
```

Keeping the lirary and not using them causes your apk to have a bigger size, and cause building and running to become slow.

### Make sure to "Gradle Sync + Clean + Build + Make" your project before running the project

Cheers : )
Apps built with AndEngine can be found in my dev account *epollomes*, feel free to check them out https://goo.gl/f5YBwy. Thanks 
