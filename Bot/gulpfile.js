var gulp = require('gulp');
var ts = require("gulp-typescript");
var tsProject = ts.createProject("tsconfig.json");

gulp.task('default', function () {
    var res = gulp.src('./resources/*')
    .pipe(gulp.dest("build/resources"));

    return tsProject.src()
    .pipe(ts(tsProject))
    .js.pipe(gulp.dest("build"));
});