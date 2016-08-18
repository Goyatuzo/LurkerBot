function generateChartFromUrl(url: string) {
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

        response.map(entry => {
            const gameIdx = _.findIndex(uniqueGames, gameName => gameName === entry.gameName);
            const nameIdx = _.findIndex(uniqueNames, name => name === entry.name);

            data[nameIdx]['data'][gameIdx] = Math.floor(entry['duration'] / 60);
        });

        return data;
    }

    $.get({
        url: url
    }).done(response => {
        response = JSON.parse(response);

        const uniqueGames = _getUniqueGames(response);
        const uniqueNames = _getUniqueNames(response);

        $(function () {
            $('#container').highcharts({
                chart: {
                    type: 'bar',
                    height: uniqueGames.length * 30 + 150
                },
                title: {
                    text: `Complete Summary of ${uniqueNames.length === 1 ? response[0].name : 'LurkerBot'}`
                },
                xAxis: {
                    categories: uniqueGames
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
}