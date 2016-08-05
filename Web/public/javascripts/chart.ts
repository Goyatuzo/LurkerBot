function _getUniqueGames(response) {
    return _.sortedUniq(response.map(entry => entry['gameName'])) as string[];
}

function _getUniqueNames(response) {
    return _.uniq(response.map(entry => entry['name'])) as string[];
}

function _getSeries(response) {
    const uniqueGames = _getUniqueGames(response);
    const uniqueNames = _getUniqueNames(response);

    const gameCount = uniqueGames.length;
    let data = [];

    uniqueNames.map(name => {
        data.push({
            name: name,
            data: _.fill(new Array(gameCount), 0)
        });
    });

    data.map(da => {
        console.log(da);
    });

    response.map(entry => {
        const gameIdx = _.findIndex(uniqueGames, gameName => gameName === entry.gameName);
        const nameIdx = _.findIndex(uniqueNames, name => name === entry.name);
        
        data[nameIdx]['data'][gameIdx] = Math.floor(entry['duration'] / 60);
    });
    
    return data;
}

$.get({
    url: "/api/summary"
}).done(response => {
    response = JSON.parse(response);

    $(function () {
        $('#container').highcharts({
            chart: {
                type: 'bar'
            },
            title: {
                text: 'Complete Summary of LurkerBot'
            },
            xAxis: {
                categories: _getUniqueGames(response)
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Minutes Played'
                }
            },
            legend: {
                reversed: true
            },
            plotOptions: {
                series: {
                    stacking: 'normal'
                }
            },
            series: _getSeries(response)
        });
    });

});