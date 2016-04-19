export JAVA_HOME=~/devel/soft/jdk/java8
export PATH="$PATH:$JAVA_HOME/bin"
echo "Using java at: $JAVA_HOME"

# set PATH so it includes user's private bin if it exists
if [ -d "$HOME/bin" ] ; then
    PATH="$HOME/bin:$PATH"
fi
