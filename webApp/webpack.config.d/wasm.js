(function (config) {
    const CopyWebpackPlugin = require("copy-webpack-plugin");
    config.plugins.push(
        new CopyWebpackPlugin({
            patterns: [
                {
                    from: "../../../js/node_modules/sql.js/dist/sql-wasm.wasm",
                    to:   "",
                },
            ],
        })
    );
})(config);