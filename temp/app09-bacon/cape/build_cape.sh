#!/bin/sh
rm *.dtbo
dtc -O dtb -o BB-BONE-BCN-00A0.dtbo -b 0 -@ BB-BONE-BCN-00A0.dts