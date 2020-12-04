#!/bin/bash

set -e

PACKR_VERSION="runelite-1.0"

rm -f 2009scape.AppImage

# Check if there's a client jar file - If there's no file the AppImage will not work but will still be built.
if ! [ -e 2009scape.jar ]
then
  echo "2009scape.jar not found, exiting"
  exit 1
fi

if ! [ -f OpenJDK8U-jre_x64_linux_hotspot_8u275b01.tar.gz ] ; then
    curl -Lo OpenJDK8U-jre_x64_linux_hotspot_8u275b01.tar.gz \
	https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u275-b01/OpenJDK8U-jre_x64_linux_hotspot_8u275b01.tar.gz
fi

rm -f packr.jar
curl -o packr.jar https://libgdx.badlogicgames.com/ci/packr/packr.jar

# packr requires a "jdk" and pulls the jre from it - so we have to place it inside
# the jdk folder at jre/
if ! [ -d linux-jdk ] ; then
    tar zxf OpenJDK8U-jre_x64_linux_hotspot_8u275b01.tar.gz 
    mkdir linux-jdk
    mv jdk8u275-b01-jre linux-jdk/jre
fi

if ! [ -f packr_${PACKR_VERSION}.jar ] ; then
    curl -Lo packr_${PACKR_VERSION}.jar \
        https://github.com/runelite/packr/releases/download/${PACKR_VERSION}/packr.jar
fi

echo "18b7cbaab4c3f9ea556f621ca42fbd0dc745a4d11e2a08f496e2c3196580cd53  packr_${PACKR_VERSION}.jar" | sha256sum -c

java -jar packr_${PACKR_VERSION}.jar \
    --platform \
    linux64 \
    --jdk \
    linux-jdk \
    --executable \
    2009scape \
    --classpath \
    2009scape.jar \
    --mainclass \
    com.fox.Launcher \
    --output \
    native-linux/2009scape.AppDir/ \
    --resources \
    appimage/2009scape.desktop \
    appimage/2009scape.png

cp appimage/2009scape.png native-linux/2009scape.AppDir

# Symlink AppRun -> 2009scape
pushd native-linux/2009scape.AppDir/
ln -s 2009scape AppRun
popd

curl -Lo appimagetool-x86_64.AppImage https://github.com/AppImage/AppImageKit/releases/download/12/appimagetool-x86_64.AppImage
chmod 755 appimagetool-x86_64.AppImage

./appimagetool-x86_64.AppImage \
	native-linux/2009scape.AppDir/ \
	native-linux/2009scape.AppImage

echo "Cleaning up.."

mv native-linux/2009scape.AppImage .
rm -rf packr packr.jar packr_runelite-1.0.jar native-linux linux-jdk OpenJDK8U-jre_x64_linux_hotspot_8u275b01.tar.gz appimagetool-x86_64.AppImage
