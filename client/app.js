const { app, BrowserWindow } = require("electron");
const url = require("url");
const path = require("path");

let mainWindow;

function createWindow() {
  mainWindow = new BrowserWindow({
    autoHideMenuBar: true,
    webPreferences: {
      nodeIntegration: true,
    },
  });
  mainWindow.maximize();
  mainWindow.setResizable(false);
  mainWindow.loadURL(
    url.format({
      pathname: path.join(__dirname, `/dist/client/index.html`),
      protocol: "file:",
      slashes: true,
    })
  );

  mainWindow.setFullScreenable(true);

  mainWindow.once("ready-to-show", () => {
    mainWindow.show();
  });

  mainWindow.on("closed", function () {
    mainWindow = null;
  });
}

app.on("ready", createWindow);

app.on("window-all-closed", function () {
  if (process.platform !== "darwin") app.quit();
});

app.on("activate", function () {
  if (mainWindow === null) createWindow();
});
