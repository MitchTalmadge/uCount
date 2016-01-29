function ManageContestantsViewModel() {
	var self = this;

	self.polls = ko.observableArray();
	self.selectedPoll = ko.observable();
	self.selectedPollData = ko.observableArray();

	self.ready = ko.observable(false);

	self.imageLocation = "/Sweethearts-Poll/contestantImg/";

	self.selectedPartner = ko.observable();

	// Behaviours
	self.selectPoll = function(poll) {
		console.log("selected Poll: " + JSON.stringify(poll));
		self.selectedPoll(poll);
		$.post("/Sweethearts-Poll/ContestantProvider", {
			request : 1,
			obj : JSON.stringify(poll)
		}).done(function(data) {
			self.selectedPollData(data);
			console.log("Contestants for Poll: " + JSON.stringify(data));
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	};
	
	self.declareIdParameter = function(contestant) {
		$.post("/Sweethearts-Poll/ParameterProvider", {
			id : contestant.id
		}).done(function(data) {
			self.selectedPollData(data);
			console.log("Contestants for Poll: " + JSON.stringify(data));
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	}

	self.fullName = function(contestant) {
		return contestant.firstName + " " + contestant.lastName;
	};

	self.fullPictureLocation = function(contestant) {
		return self.imageLocation + contestant.pictureFileName;
	};

	self.toggleReady = function() {
		self.ready(!self.ready());
		console.log("Ready = " + self.ready());
	};

	// Data Pull
	$.ajax({
		url : '/Sweethearts-Poll/PollProvider',
		dataType : "json",
		method : 'POST',
		success : function(data) {
			console.log("Data received:" + JSON.stringify(data));
			self.polls(data);
			if (data != "") {
				self.selectPoll(self.polls()[0]);
			}
			self.ready(true);
		},
		failure : function(error) {
			console.log(error);
		}
	});
}

$(document).ready(function() {
	$('.collapsible').collapsible({
		accordion : true
	// A setting that changes the collapsible behavior to expandable instead of
	// the default accordion style
	});
});

ko.applyBindings(new ManageContestantsViewModel());
