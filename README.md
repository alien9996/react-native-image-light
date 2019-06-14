
# react-native-image-light

**Libraries add mode lighting effects to your images**

## Demo
![gif](https://github.com/alien9996/react-native-image-light/blob/master/Light.gif?raw=true)

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
  - Add `import com.reactlibraryimagelight.RNImageLightPackage;` to the imports at the top of the file
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


## Example
### You have two choices to use the library.

1. **Resource use is available.**
```javascript
import RNImageLight from 'react-native-image-light';

RNImageLight.getResourcesImageLight({
	imageSource1: "/storage/emulated/0/Download/img.jpg",
	imageSource2: null,
	dataType1: "Path",
	dataType2: "Path",
	overlayType: 3,
	isAccsets: true
	}, (source) => {
		this.setState(imgBase64 : source.base64);
		console.log("SOURCE", source);
		// "source" returns the height, width and the Base64 string of the image.
	});
```
**The result you get will be the same as the demo**

2. **Use an external rescource of your**

```javascript
import RNImageLight from 'react-native-image-light';

RNImageLight.getResourcesImageLight({
            imageSource1: "/storage/emulated/0/Download/img.jpg",
            imageSource2: "/storage/emulated/0/Download/img2.jpg",
            dataType1: "Path",
            dataType2: "Path",
            overlayType: 0,
            isAccsets: false
          }, (source) => {
           	this.setState(imgBase64 : source.base64);
			console.log("SOURCE", source);
			// "source" returns the height, width and the Base64 string of the image.
          });
```
**Note**: To get the most perfect picture, you should send to **imageSource1** and **imageSource2** images of similar size.<br>
**You will get the following result**

![Demo1](https://github.com/alien9996/react-native-image-light/blob/master/demo.png?raw=true)

## Options

Props | Default | Options/Info
------ | --- | ------
imageSource1 (String)|null|The path to the image in the device or a Base64 string.
imageSource2 (String)|null|The path to the image in the device or a Base64 string.
dataType1 (String)|Path|If you send a path, enter the string "Path"<br>If you send a Base64 string, enter the string "Base64".
dataType2 (String)|Path|If you send a path, enter the string "Path"<br>If you send a Base64 string, enter the string "Base64".<br> **Note**: Valid only when isAccsets = false.
overlayType (int)|0|Select the type you want to process images, the values from 0 to 26. Other values around 0 to 26 will not take effect.<br> **Note**: Valid only when isAccsets = true.
isAccsets (boolean)|true|If you want use the resource, select **true**.<br>
If you do not want use resource, select **false**.

## Filter types

![filterType](https://github.com/alien9996/react-native-image-light/blob/master/overlay_type.png?raw=true)

## Note
- The image path you send into **imageSource1:''** and **imageSource2:''**  must be the absolute path. If you have problems with the absolute path, you can find the solution [here](https://stackoverflow.com/questions/52423067/how-to-get-absolute-path-of-a-file-in-react-native).

### Thank you for your interest!