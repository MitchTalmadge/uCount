function ManageContestantViewModel() {
	var self = this;
	
	//Data
	self.pollsFull = $.get("/PollProvider");
	self.polls = [];
	for(var i = 0; i < pollsFull.length; i++) {
	    polls[i] = pollsFull[i].name;
	}
	
	
	self.selectedPoll = ko.observable();
	self.selectedPollData = ko.observable();
	self.selectedContestantData = ko.observable();
	
	// Behaviours
    self.selectPoll = function(poll) { 
        self.selectedPoll(poll);
        self.selectedContestantData(null);
        $.get('/ContestantProvider', { pollName: poll }, self.selectedPollData);
    };
    
    self.selectPoll(polls[0]);
    
    self.selectContestant = function(contestant) { 
        self.selectedPoll(contestant.pollName);
        self.selectedPollData(null); // Stop showing a folder
        $.get("/ContestantProvider", { id: contestant.id }, self.selectedContestantData);
    };
    
    self.fullName = ko.computed(function(contestant) {
    	return contestant.firstName() + " " + contestant.lastName();
    });
	
}

ko.applyBindings(new ManageContestantViewModel());