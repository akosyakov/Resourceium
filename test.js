// @ts-check
const { spawn } = require('child_process');
async function main() {
    await new Promise((resolve, reject) => {
        const mark = "4c9babf79fc6";
        const child = spawn("/bin/bash", [...["-ilc"], `'${process.execPath}' -p '"${mark}" + JSON.stringify(process.env) + "${mark}"'`], {
            detached: true,
            stdio: ['ignore', 'pipe', 'pipe'],
            env: {
                ...process.env,
                ELECTRON_RUN_AS_NODE: '1',
                ELECTRON_NO_ATTACH_CONSOLE: '1'
            }
        });

        child.on('error', err => {
            console.error('getUnixShellEnvironment#errorChildProcess', err);
            resolve({});
        });

        const buffers = [];
        child.stdout.on('data', b => {
            buffers.push(b);
        });

        const stderr = [];
        child.stderr.on('data', b => {
            stderr.push(b);
        });

        child.on('exit', (code, signal) => {
            console.info('getUnixShellEnvironment#exit', code, signal);
        });

        child.on('close', (code, signal) => {
            console.info('getUnixShellEnvironment#close', code, signal);

            const raw = Buffer.concat(buffers).toString('utf8');
            console.info('getUnixShellEnvironment#raw', raw);

            const stderrStr = Buffer.concat(stderr).toString('utf8');
            if (stderrStr.trim()) {
                console.info('getUnixShellEnvironment#stderr', stderrStr);
            }

            if (code || signal) {
                return reject(new Error(`Failed to get environment (code ${code}, signal ${signal})`));
            }
            resolve();
        });
    });
}
main();