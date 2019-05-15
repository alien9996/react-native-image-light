using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Image.Light.RNImageLight
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNImageLightModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNImageLightModule"/>.
        /// </summary>
        internal RNImageLightModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNImageLight";
            }
        }
    }
}
