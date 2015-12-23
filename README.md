# AndEngine-Template-AndroidStudio
This is a Template Project for AndEngine, to start developing a game with Andengine, Included are the Box2D extension and the DebugDraw to debug the physics

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

##! Make sure to "Gradle Sync + Clean + Build + Make" your project before trying to run 
