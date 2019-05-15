
# react-native-image-light

## Getting started

`$ npm install react-native-image-light --save`

### Mostly automatic installation

`$ react-native link react-native-image-light`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-image-light` and add `RNImageLight.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNImageLight.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNImageLightPackage;` to the imports at the top of the file
  - Add `new RNImageLightPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-image-light'
  	project(':react-native-image-light').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-image-light/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-image-light')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNImageLight.sln` in `node_modules/react-native-image-light/windows/RNImageLight.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Image.Light.RNImageLight;` to the usings at the top of the file
  - Add `new RNImageLightPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNImageLight from 'react-native-image-light';

// TODO: What to do with the module?
RNImageLight;
```
  