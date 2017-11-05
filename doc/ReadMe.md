Stetho
http://www.jianshu.com/p/03da9f91f41f
http://www.jianshu.com/p/a7fdcb2641e8

运行App, 打开Chrome输入chrome://inspect/#devices（别忘了用数据线把手机和电脑连起来哦）

兼容库的使用
http://www.jianshu.com/p/1e6eed09d48b


## 主题
系统自带主题：
API 1:
android:Theme 根主题
android:Theme.Black 背景黑色
android:Theme.Light 背景白色
android:Theme.Wallpaper 以桌面墙纸为背景
android:Theme.Translucent 透明背景
android:Theme.Panel 平板风格
android:Theme.Dialog 对话框风格

API 11:
android:Theme.Holo Holo根主题
android:Theme.Holo.Black Holo黑主题
android:Theme.Holo.Light Holo白主题

API 14:
Theme.DeviceDefault 设备默认根主题
Theme.DeviceDefault.Black 设备默认黑主题
Theme.DeviceDefault.Light 设备默认白主题

API 21: (网上常说的 Android Material Design 就是要用这种主题)
Theme.Material Material根主题
Theme.Material.Light Material白主题


兼容包v7中带的主题：
Theme.AppCompat 兼容主题的根主题
Theme.AppCompat.Black 兼容主题的黑色主题
Theme.AppCompat.Light 兼容主题的白色主题

Theme.AppCompat主题是兼容主题，是什么意思呢？

意思就是说如果运行程序的手机API是21则就相当于是Material主题，如果运行程序的手机API是11则就相当于是Holo主题，以此类推
所有能应用于应用程序主题都是以“Theme.”开头
不是以“Theme.”开头的就不是应用程序主题，而是用于某些局部控件
比如“ThemeOverlay”主题，可用于 Toolbar 控件，这里不做深入分析了。
比如“TextAppearance”主题，可用于设置文字外观，这里不做深入分析了。
比如在v7中有很多以“Base”开头的主题，是一些父主题，不建议直接使用

Black 黑色风格
Light 光明风格
Dark 黑暗风格
DayNight 白昼风格
Wallpaper 墙纸为背景
Translucent 透明背景
Panel 平板风格
Dialog 对话框风格
NoTitleBar 没有TitleBar
NoActionBar 没有ActionBar
Fullscreen 全屏风格
MinWidth 对话框或者ActionBar的宽度根据内容变化，而不是充满全屏
WhenLarge 对话框充满全屏
TranslucentDecor 半透明风格
NoDisplay 不显示，也就是隐藏了
WithActionBar 在旧版主题上显示ActionBar

很多主题在使用时会报错，原因有很多，比如窗体必须继承AppCompactActivity，或者要继承ActionBarActiivty，或者要继承FragmentActivity，或者需要手动指定宽高，或者需要提升最低API版本，或者需要更高版本的SDK，或者兼容包版本不对等原因。

