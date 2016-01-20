function ManageContestantViewModel() {
	var self = this;

	self.polls = ko.observable();
	self.selectedPoll = ko.observable();
	self.selectedPollData = ko.observable();
	self.selectedContestantData = ko.observable();
	
	self.ready = ko.observable(false);
	self.contestantCollectionReady = ko.observable(false);
	self.contestantInfoReady = ko.observable(false);

	// Behaviours
	self.selectPoll = function(poll) {
		self.contestantCollectionReady(false);
		self.contestantInfoReady(false);
		console.log("selected Poll: " + JSON.stringify(poll));
		self.selectedPoll(poll);
		self.selectedContestantData(null);
		$.post("ContestantProvider", {
			request : 1,
			obj : JSON.stringify(poll)
		}).done(function(data) {
			self.selectedPollData(data);
			console.log("Contestants for Poll: " + JSON.stringify(data));
			self.contestantCollectionReady(true);
			console.log("Visible after contestants: " + self.ready());
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	};

	self.selectContestant = function(contestant) {
		self.contestantInfoReady(false);
		self.contestantCollectionReady(false);
		self.selectedPoll(contestant.pollName);
		self.selectedPollData(null); // Stop showing a folder
		$.post("ContestantProvider", {
			request : 0,
			obj : JSON.stringify(contestant)
		}).done(function(data) {
			self.selectedContestantData(data);
			self.contestantInfoReady(true);
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	};

	self.fullName = function(contestant) {
		return contestant.firstName + " " + contestant.lastName;
	};
	
	self.toggleReady = function() {
		self.ready(!self.ready());
		console.log("Ready = " + self.ready());
	};

	// Data Pull
	$.ajax({
		url : 'PollProvider',
		dataType : "json",
		method : 'POST',
		success : function(data) {
			console.log("Data received:" + JSON.stringify(data));
			self.polls(data);
			self.ready(true);
			self.selectPoll(self.polls()[0]);
		},
		failure : function(error) {
			console.log(error);
		}
	});
}

ko.applyBindings(new ManageContestantViewModel());
