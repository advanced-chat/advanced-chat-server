export default {
    '*': (stagedFiles) => {
        const changed = stagedFiles.some((f) =>
            /\.(java|kt|kts|md|sql)$/i.test(f)
        );

        if (!changed) return [];

        return ['./gradlew --quiet spotlessApply'];
    },
};
