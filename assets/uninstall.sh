#require su before start

#remount /system
mount -o rw,remount /system

#clean
rm /system/bin/dolphincall /system/bin/dolphinsu /system/bin/dolphinget
mount -o ro,remount /system

