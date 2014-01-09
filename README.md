Real Estate Sample App
======================

A sample real estate app showing how to:
  - use Maven build system with multiple modules
  - use Robolectric and Robotium for testing
  - use Navigation Drawer and ActionBarCompat
  - support multiple screen sizes
  - use Event Bus for decoupling fragments and activities
  - use Picasso for image loading and Retrofit for HTTP requests

Known issues on IntelliJ
=====
**Problem**: When running a Robolectric Test a "Stub!" exceptions occurs.<br>
**Solution**: Open Module Settings -> Dependencies and move Android framework to the bottom

**Problem**: When running a Robolectric Test *AndroidManifest.xml* is not found.<br>
**Solution**: A correct module needs to be selected. Edit run configuration and select *real-estate* instead of *real-estate-parent* for *Use classpath of module*

**Problem**: No Android SDK selected.<br>
**Solution**: Select *real-estate-all*, right-click and select Open Module Settings. Select your Java and Android SDK locations.

