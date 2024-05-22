const { contextBridge, ipcRenderer } = require('electron/renderer')

contextBridge.exposeInMainWorld('electronAPI', {
  setTest: (test) => ipcRenderer.send('set-test', test),
  getLogs: (callback)=>ipcRenderer.on('log-msg', (event,logs)=>callback(logs)),
  getReport: (callback)=>ipcRenderer.on('report-msg', (event,report)=>callback(report)),
  rmEventListener:(channel)=>ipcRenderer.removeAllListeners(channel)
})