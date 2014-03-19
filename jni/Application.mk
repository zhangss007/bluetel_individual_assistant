APP_PROJECT_PATH := $(call my-dir)/../
APP_MODULES      :=libspeex libgsm libortp libosip2 libeXosip2 libmediastreamer2 liblinphone liblinphonenoneon libneon
APP_STL := stlport_static

#uPnp
ifeq ($(BUILD_UPNP),1)
APP_MODULES += libupnp
endif

#remote provisioning
ifeq ($(BUILD_REMOTE_PROVISIONING),1)
APP_MODULES += liblpxml2 libxml2lpc liblpc2xml
endif

#default values
ifeq ($(BUILD_AMRNB),)
BUILD_AMRNB=light
endif
ifeq ($(BUILD_AMRWB),)
BUILD_AMRWB=0
endif
ifeq ($(BUILD_SRTP),)
BUILD_SRTP=1
endif

ifeq ($(LINPHONE_VIDEO),1)
APP_MODULES += liblinavutil liblinavcore liblinavcodec liblinswscale
APP_MODULES += liblinavcodecnoneon
APP_MODULES += libvpx
endif

_BUILD_AMR=0
ifneq ($(BUILD_AMRNB), 0)
_BUILD_AMR=1
endif

ifneq ($(BUILD_AMRWB), 0)
_BUILD_AMR=1
endif

ifneq ($(_BUILD_AMR), 0)
APP_MODULES += libopencoreamr libmsamr
endif

ifneq ($(BUILD_AMRWB), 0)
APP_MODULES += libvoamrwbenc
endif

ifeq ($(BUILD_X264),1)
APP_MODULES +=libx264 libmsx264
endif

ifeq ($(BUILD_SILK),1)
APP_MODULES +=libmssilk
endif

ifeq ($(BUILD_G729),1)
APP_MODULES +=libbcg729 libmsbcg729
endif

ifneq ($(BUILD_WEBRTC_AECM), 0)
APP_MODULES += libwebrtc_system_wrappers libwebrtc_spl libwebrtc_apm_utility libwebrtc_aecm
APP_MODULES += libwebrtc_spl_neon libwebrtc_aecm_neon
endif

ifeq ($(RING),yes)
APP_MODULES      += libring
endif

ifeq ($(BUILD_TUNNEL), 1)
APP_MODULES += libtunnelclient
endif

ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
APP_MODULES += liblincrypto liblinssl
APP_MODULES      +=libmsilbc

ifeq ($(BUILD_GPLV3_ZRTP), 1)
APP_MODULES      += libzrtpcpp
endif

ifeq ($(BUILD_SRTP), 1)
APP_MODULES      += libsrtp
endif
endif #armeabi-v7a


linphone-root-dir:=$(APP_PROJECT_PATH)

APP_BUILD_SCRIPT:=$(call my-dir)/Android.mk
APP_PLATFORM := android-8
APP_ABI := armeabi-v7a armeabi
ifeq ($(BUILD_FOR_X86), 1)
APP_ABI += x86
endif
APP_CFLAGS:=-DDISABLE_NEON
