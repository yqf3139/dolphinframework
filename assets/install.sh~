#require su before start

#remount /system
mount -o rw,remount /system

#install dolphincall
cat /storage/sdcard0/project_dolphin/dolphincall > /system/bin/dolphincall
chmod 755 /system/bin/dolphincall
#install dolphinget
cat /storage/sdcard0/project_dolphin/dolphinget > /system/bin/dolphinget
chmod 755 /system/bin/dolphinget
#install dolphinsu
cat /storage/sdcard0/project_dolphin/dolphinsu > /system/bin/dolphinsu
chmod 4755 /system/bin/dolphinsu
#init

#clean
rm /storage/sdcard0/project_dolphin/dolphincall /storage/sdcard0/project_dolphin/dolphinsu /storage/sdcard0/project_dolphin/dolphinget
mount -o ro,remount /system

