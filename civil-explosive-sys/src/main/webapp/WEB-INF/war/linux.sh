#!/bin/sh

#
#参数:
#
#

if [ $# -ne 3 ]
then
	echo "参数不正确"
	exit -1
fi

basepath=$1
warfile=$2
warname=$3
#停止服务
"${basepath}/bin/shutdown.sh"
#复制文件
rm -f "${basepath}/webapps/${warname}.war"
rm -fr "${basepath}/webapps/${warname}"
cp -fv  "${basepath}${warfile}"	 "${basepath}/webapps/${warname}.war"

#延迟后启动服务
sleep 6

"$basepath/bin/startup.sh"


