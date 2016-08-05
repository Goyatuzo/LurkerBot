var gulp = require('gulp');
var ts = require("gulp-typescript");
var tsProject = ts.createProject("tsconfig.json");

gulp.task('default', function () {
    gulp.src('./public/*')
        .pipe(gulp.dest("build/public"));

    gulp.src('./views/*')
        .pipe(gulp.dest("build/views"));

    return tsProject.src()
        .pipe(ts(tsProject))
        .js.pipe(gulp.dest("build"));
});